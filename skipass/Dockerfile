FROM teamb/w4e-skipass-service-builder AS builder

###################
# prod
FROM gcr.io/distroless/nodejs20-debian12 AS prod

WORKDIR /app

COPY --from=builder /usr/src/app/node_modules ./node_modules
COPY --from=builder /usr/src/app/dist ./dist
COPY docker-healthcheck/healthcheck.js .

EXPOSE 9092
# Start the server using the production build
CMD ["dist/main.js" ]
