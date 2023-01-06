FROM python:3.11-bullseye

RUN apt-get update -y 

COPY . /app
WORKDIR /app
ARG DJANGO_LISTEN "0.0.0.0"
ARG DJANGO_PORT 8000
ENV DJANGO_LISTEN ${DJANGO_LISTEN}
ENV DJANGO_PORT ${DJANGO_PORT}

RUN python -m pip install -r requirements.txt
#RUN python manage.py makemigrations
#RUN python manage.py migrate
RUN python manage.py collectstatic

EXPOSE ${DJANGO_PORT}
CMD python manage.py runserver ${DJANGO_LISTEN}:${DJANGO_PORT}
