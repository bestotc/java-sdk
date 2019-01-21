JAVA SDK 

API文档请访问:
https://github.com/bestotc/APIDOC/wiki

订单相关操作:com.bestotc.service.ApiOtcOrderService
订单异步通知:com.bestotc.service.ApiOrderNotifyService
IM异步通知:com.bestotc.service.ApiImNotifyService

使用方法:
1.使用maven package 后会生成如下三个jar包 
    otc-java-sdk-1.0.jar
    otc-java-sdk-1.0-sources.jar(源码包) 
    otc-java-sdk-1.0-jar-with-dependencies.jar(带依赖的jar:避免与您项目依赖jar版本差异而无法运行的情况)
2.将jar包放在您项目libs目录下,maven 中添加如下内容:
   <dependency>
      <groupId>com.bestotc</groupId>
      <artifactId>otc-java-sdk</artifactId>
      <version>1.0</version>
      <scope>system</scope>
      <systemPath>${project.basedir}/libs/otc-java-sdk-1.0.jar</systemPath>
      <!--<systemPath>${project.basedir}/libs/otc-java-sdk-1.0-jar-with-dependencies.jar</systemPath>-->
     </dependency>
3.创建相应的service:
     private static ApiOrderNotifyService apiOrderNotifyService = new ApiOrderNotifyService("QHqNZXZZ2XRm");
