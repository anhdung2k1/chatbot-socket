# Use OpenJDK with a specified version (default: 17-oracle)
ARG VERSION=17-oracle
FROM openjdk:$VERSION

# Set Java options
ENV JAVA_OPTS=""

# Define a build-time argument for the UID
ARG UID=1001

# Create a group and user with the specified UID
RUN groupadd -g ${UID} socket-server-group && \
    useradd -m -u ${UID} -g socket-server-group -d /home/socket-server socket-server

# Switch to the non-root user
USER socket-server

# Set working directory
WORKDIR /app

# Copy only the `-all.jar` file
COPY --chown=${UID}:${UID} target/chatbot-socket-server-*-all.jar chatbot-server.jar

EXPOSE 8000

# Run the application
CMD ["java", "-jar", "chatbot-server.jar"]