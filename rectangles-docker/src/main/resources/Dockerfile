FROM openjdk:alpine
RUN apk add --update bash && rm -rf /var/cache/apk/*
ADD @project.build.finalName@.jar /
ADD start /
RUN chmod +x /start
ENTRYPOINT ["/start"]