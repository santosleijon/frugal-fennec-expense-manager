FROM postgres:13

ENV POSTGRES_PASSWORD frugal_fennec

COPY ./migrations/ /docker-entrypoint-initdb.d/

EXPOSE 5432
