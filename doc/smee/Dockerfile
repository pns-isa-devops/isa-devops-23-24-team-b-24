FROM node:21-alpine3.18

# Install smee-client globally
RUN npm install -g smee-client

# Set the working directory
WORKDIR /app

# Set the entrypoint command to run smee
ENTRYPOINT ["smee"]

# Specify the command line arguments for smee
CMD ["--url", "https://smee.io/zn1y3yFzx0Qq2WVz", "--target", "http://jenkins:8080/multibranch-webhook-trigger/invoke?token=trAxvd7Yf4L26L5m2hMZkGjfpVmFceuwLE8WksxqB4p"]