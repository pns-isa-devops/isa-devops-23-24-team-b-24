FROM teamb/w4e-scheduler-service-builder AS builder

###################
# prod
FROM gcr.io/distroless/nodejs20-debian12 AS prod

WORKDIR /app

COPY --from=builder /usr/src/app/node_modules ./node_modules
COPY --from=builder /usr/src/app/dist ./dist
COPY docker-healthcheck/healthcheck.js .

EXPOSE 9091
# Start the server using the production build
CMD ["dist/main.js" ]
