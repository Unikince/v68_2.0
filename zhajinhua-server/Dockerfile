#指定基础镜像，在其上进行定制
FROM anapsix/alpine-java:8_server-jre_unlimited

#维护者信息
MAINTAINER ambitiousmice <ambitiousmice@gmail.com>

#时间设置
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /zhajinhua

WORKDIR /zhajinhua

#复制上下文目录下的target/服务jar包.jar 到容器里
COPY target/zhajinhua-server.jar zhajinhua-server.jar

#bash方式执行，使zhajinhua-server.jar可访问
#RUN新建立一层，在其上执行这些命令，执行结束后， commit 这一层的修改，构成新的镜像。
RUN bash -c "touch /zhajinhua-server.jar"

#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8041

CMD java -Djava.security.egd=file:/dev/./urandom -jar zhajinhua-server.jar