FROM nginx:1.23.1-alpine

RUN mkdir /app
WORKDIR /app
COPY build /app

COPY docker/nginx.conf /etc/nginx/nginx.conf

COPY docker/entrypoint.sh /usr/local/bin/entrypoint
RUN chmod +x /usr/local/bin/entrypoint

ENV REACT_APP_API_URL="localhost:8080"

EXPOSE 3000/tcp

ENTRYPOINT [ "entrypoint" ]

CMD ["/usr/sbin/nginx", "-g", "daemon off;"]
