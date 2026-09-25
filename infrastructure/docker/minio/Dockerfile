FROM golang:1.24-alpine AS builder

ARG MINIO_VERSION

RUN apk add --no-cache git
WORKDIR /src
RUN git clone --depth 1 --branch "${MINIO_VERSION}" https://github.com/minio/minio.git . \
    && go build -trimpath -ldflags="-s -w" -o /out/minio .

FROM alpine:3.22

RUN apk add --no-cache wget

COPY --from=builder /out/minio /usr/local/bin/minio

VOLUME ["/data"]
ENTRYPOINT ["/usr/local/bin/minio"]
CMD ["server", "/data", "--console-address", ":9001"]