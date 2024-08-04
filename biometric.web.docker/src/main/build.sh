#!/bin/bash

# Michael O'Brien

TARGET_DIR=../../target
mkdir $TARGET_DIR

cp ../../../biometric.web/target/*.war $TARGET_DIR
cp DockerFile $TARGET_DIR
#docker build -t obrienlabs/biometric-web -f DockerFile $TARGET_DIR
#docker images | grep biometric-web
#docker tag obrienlabs/biometric-web obrienlabs/biometric-web:0.0.2
#docker login
#docker push obrienlabs/biometric-web:0.0.2
docker build --no-cache --build-arg build-id=10002 -t biometric-web -f DockerFile $TARGET_DIR
docker tag biometric-web:latest biometric-web:latest
docker stop biometric_web
docker rm biometric_web
#docker run -d -it --rm --name biometric -p 8888:8080 biometric-web:latest
docker run --name biometric_web -d -p 8888:8080 -v ~/Dropbox/env/mbp4/biometric/tomcat-users.xml:/usr/local/tomcat/conf/tomcat-users.xml \
  -v ~/Dropbox/env/mbp4/biometric/setenv.sh:/usr/local/tomcat/bin/setenv.sh \
-e os.environment.configuration.dir=/ \
-e os.environment.db.biometric.schema=biometric \
-e os.environment.db.url=127.0.0.1:3406/biometric?useSSL=true \
-e os.environment.db.username=obrienlabs \
-e os.environment.db.password=oc \
-e os.environment.discriminator=biometric \
-e os.environment.ecosystem=prod biometric-web:latest

#docker stop kubernetes-viewport
#docker image rm oomk8s/kubernetes-viewport
#docker rmi $(docker images -f "dangling=true" -q)
#cd ..
#mvn clean install -U
#cd kubernetes-viewport-docker-root/
#./build.sh
#docker run -d -it --rm --name biometric -p 8888:8080 biometric-web:latest
#docker logs -f kubernetes-viewport
#curl http://127.0.0.1:8888/kubernetes-viewport/rest/health/health

