FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 设置时区（可选）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup
USER appuser

# 将构建好的 Spring Boot JAR 文件复制到容器中
# 假设你的 jar 文件名为 app.jar，放在 target/ 目录下
COPY --chown=appuser:appgroup target/*.jar app.jar

# 设置 JVM 参数以限制内存使用（总堆 + 非堆 <= 500MB）
# -Xmx350m：最大堆内存
# -XX:MaxMetaspaceSize=100m：限制 Metaspace
# -XX:+UseG1GC：使用 G1 垃圾回收器（适合容器环境）
# -XX:+UseContainerSupport：启用容器支持（默认已启用，但显式声明更清晰）
# -XX:MaxRAMPercentage=75.0：也可用百分比方式控制，但这里我们用固定值更精确
ENV JAVA_OPTS="-Xmx350m -Xms350m -XX:MaxMetaspaceSize=100m -XX:+UseG1GC -XX:+UseContainerSupport"

EXPOSE 8123

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]