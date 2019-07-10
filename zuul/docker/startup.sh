#!/bin/bash
projectname="zuul_v"
#首先验证镜像是否存在
images_id=$(docker images | grep "$projectname" | awk "{print \$3}")
if [ -n "$images_id" ]; then
    echo "存在$projectname 的镜像id=$images_id ................"
	is_stop=$(docker ps | grep "$projectname" | awk "{print \$1}")
	echo "contener id =$is_stop .................."
	if [ -n "$is_stop" ]; then
		echo "$projectname 还有容器id=$is_stop没有关闭......"
		docker stop "$is_stop"
		echo "$projectname 容器id=$is_stop 已经关闭......"
		echo "对$projectname 容器id=$is_stop 进行删除操作......"
		docker ps -a | grep "$projectname"|awk '{print $1}' | xargs docker rm
		echo "$projectname 容器id=$is_stop 已经删除......"
		echo ".....................进行镜像操作.............................."
		docker rmi -f $(docker images | grep "$projectname" | awk "{print \$3}")
		echo "$projectname 容器id=$is_stop对应的镜像 已经删除......"
	else
		docker rmi -f $(docker images | grep "$projectname" | awk "{print \$3}")
		echo "$projectname 容器id=$is_stop对应的镜像 已经删除......"
	fi
else
    echo "存在$projectname 的镜像 ................ "
fi

cd ../
mvn install
cd /home/tx_workspace/project_git/zuul/target &&
cp zuul-0.0.1-SNAPSHOT.jar ../docker
cd docker &&
docker build -t alex/zuul:zuul_v1.0 .