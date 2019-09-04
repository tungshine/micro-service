# base image
FROM registry.cn-hangzhou.aliyuncs.com/tangxu/alex:openjdk
MAINTAINER tom "362615233@qq.com"
VOLUME /tmp
ENV VERSION 1.0.0
ENV JAVA_OPTS=""
ENV  MYPATH="/account"
RUN mkdir -p ${MYPATH}
WORKDIR ${MYPATH}
RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
ADD  service-zuul-0.0.1-SNAPSHOT.jar  ${MYPATH}/service-zuul_${VERSION}.jar
ENTRYPOINT ["sh", "-c", "java  ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom  -jar ${MYPATH}/service-zuul_${VERSION}.jar && tail -f /dev/null"]
EXPOSE 7073