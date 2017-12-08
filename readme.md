<h1>1、maven多环境部署</h1>
> 1.同一套程序可配置多套配置文件
> 2.在类路径下resource新建dev、pro、test三个文件夹分别存放研发、生成、测试环境下的配置文件
> 3. 除了conf-sys.properties文件中的propertiesName（指定当前环境）字段不同其他都想用
> 4. maven 发布注意：如果有些包并不是在公共仓库中，那么就需要搭建mvaen库然后再User Setting 中配置好maven的配置文件
> 5. pom文件的配置：
	根标签下：
	
	<!-- 配置文件 -->
	<profiles>
		<profile><!-- 研发环境 -->
			<id>development</id>
			<properties>
				<profiles.active>dev</profiles.active><!-- Tomcat7的部署url是manager/text 
					Tomcat6没有/text -->
				<deploy.url>http://192.168.191.2:8080/manager/text</deploy.url>
			</properties>
		</profile>
		<profile><!-- 测试环境 -->
			<id>test</id>
			<properties>
				<profiles.active>test</profiles.active>
				<deploy.url>http://192.168.0.200:8080/manager/text</deploy.url>
			</properties>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
		</profile>
		<profile>
			<!-- 生产环境 (出于安全考虑，一般生产环境不能自动部署，也不会开放端口，需要人工处理，这里的配置用来打包) -->
			<id>production</id>
			<properties>
				<profiles.active>pro</profiles.active>
				<deploy.url>http://www.app.cn:8080/manager/text</deploy.url>
			</properties>
		</profile>
	</profiles>
	
	build标签下：
	忽略test、pro、dev目录下的配置文件.根据<profile>中<activation>
				<activeByDefault>true</activeByDefault>
			</activation>在resource下生成一份配置文件！
	
	<!-- 打包配置文件管理 -->
		<resources>

			<!-- 代码生成器配置文件过滤 -->
			<resource>
				<directory>src/main/java</directory>
				<includes>
					<include>**/*.properties</include>
					<include>**/*.xml</include>
				</includes>
				<excludes>
					<exclude>**/generatorConfig.xml</exclude>
				</excludes>
				<filtering>true</filtering>
			</resource>

			<!-- 所有环境配置文件过滤 -->
			<resource>
				<directory>src/main/resources</directory>
				<excludes>
					<exclude>test/*</exclude>
					<exclude>pro/*</exclude>
					<exclude>dev/*</exclude>
				</excludes>
			</resource>

			<!-- 指定环境配置文件导入 -->
			<resource>
				<directory>src/main/resources/${profiles.active}</directory>
			</resource>
		</resources>
	
> 6. [参考这个帖子有很大价值](http://blog.csdn.net/chwshuang/article/details/52923268)	

	6.1.研发环境打包指令
	
	名称		指令		说明
	Name	dev-dev-package	显示名称
	Base directory	${workspace_loc:/june_web_new}	项目绝对地址目录
	Goals	clean package -Ddev	maven执行指令 指令中的-D表示参数，如果是研发环境就是dev，生产是pro
	Skip Tests	选中	选中表示跳过测试

	6.2. 研发环境部署指令
	
	名称		指令		说明
	Name	dev-dev-deploy	显示名称
	Base directory	${workspace_loc:/june_web_new}	项目绝对地址目录
	Goals	cargo:redeploy -Ddev	maven执行指令 指令中的-D表示参数，如果是研发环境就是dev，生产是pro
	Skip Tests	选中	选中表示跳过测试

	6.3. 测试环境打包指令
	
	名称		指令		说明
	Name	dev-test-package	显示名称
	Base 	directory	${workspace_loc:/june_web_new}	项目绝对地址目录
	Goals	clean package -Dtest	maven执行指令 指令中的-D表示参数，如果是研发环境就是dev，生产是pro
	Skip Tests	选中	选中表示跳过测试

	6.4. 测试环境部署指令
	
	名称		指令		说明
	Name	dev-test-deploy	显示名称
	Base directory	${workspace_loc:/june_web_new}	项目绝对地址目录
	Goals	cargo:redeploy -Dtest	maven执行指令 指令中的-D表示参数，如果是研发环境就是dev，生产是pro
	Skip Tests	选中	选中表示跳过测试

	6.5. 生产环境打包指令
	
	名称		指令		说明
	Name	dev-pro-package	显示名称
	Base directory	${workspace_loc:/june_web_new}	项目绝对地址目录
	Goals	clean package -Dpro	maven执行指令 指令中的-D表示参数，如果是研发环境就是dev，生产是pro
	Skip Tests	选中	选中表示跳过测试
	
<h1>2、自动发布到tomcat</h1>
<ul>
<ol>1利用maven插件可将生成的war自动发布到远端和本地的tomcat</ol>
<ol>2在tomcat中配置用户权限,即添加管理员帐号</ol>
<ol>3在maven中添加server,配置tomcat的管理员帐号密码</ol>
<ol>4在project中添加插件,以及maven中配置的server,</ol>
<ol>5设置部署命令</ol>
<ol>6进行部署</ol>
<ol>7附相关错误及解决办法</ol></ul>


<!-- 1\自动部署 -->
			<plugin>
				<groupId>org.apache.tomcat.maven</groupId>
				<artifactId>tomcat7-maven-plugin</artifactId>
				<configuration>
					<url>http://192.168.191.2:8080/manager/text</url>
					<server>admin</server>
					<username>tomcat-username</username>
					<password>tomcat-password</password>
					<path>/${finalName}</path>
				</configuration>
			</plugin>
			<!-- 2\自动部署 -->
			<!-- <plugin>
				<groupId>org.codehaus.cargo</groupId>
				<artifactId>cargo-maven2-plugin</artifactId>
				<version>1.1.3</version>
				<configuration>
					<wait>true</wait>
					<container>
						<containerId>tomcat7x</containerId>
						<type>remote</type>
					</container>
					<configuration>
						<type>runtime</type>
						<properties>
							<cargo.remote.uri>${deploy.url}</cargo.remote.uri>
							<pre name="code" class="html">                                                        
							Tomcat配置的用户名密码， 见Tomcat自动部署支持章节
								<cargo.remote.username>tomcat-username</cargo.remote.username>
								<cargo.remote.password>tomcat-password</cargo.remote.password>
							</pre>
						</properties>
					</configuration>
				</configuration>
			</plugin>

