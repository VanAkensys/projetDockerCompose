FROM nginx:stable-alpine

WORKDIR /usr/share/nginx/html

COPY browser/ ./
RUN chmod -R 755 . && chown -R nginx:nginx .

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
