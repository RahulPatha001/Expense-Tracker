FROM python:3.13.5
WORKDIR /app

COPY dist/ds_service-1.tar.gz .

RUN pip install --no-cache-dir ds_service-1.tar.gz

ENV FLASK_APP=src/app/__init__.py

EXPOSE 8010

CMD [ "flask", "run", "--host=0.0.0.0", "--port=8010" ]