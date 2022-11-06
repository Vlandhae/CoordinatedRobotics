from django.urls import re_path

from . import consumers

websocket_urlpatterns = [
    re_path(r"ws/car/(?P<car_name>\w+)/$", consumers.CarConsumer.as_asgi()),
]