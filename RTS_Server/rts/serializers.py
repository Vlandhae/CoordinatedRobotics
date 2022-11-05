from rest_framework import serializers
from rts.models import Somedata, Car, CarSession

class SomedataSerializer(serializers.ModelSerializer):
    class Meta:
        model = Somedata
        fields = ["key", "value"]


class CarSerializer(serializers.ModelSerializer):
    class Meta:
        model = Car
        fields = ["name", "current_command"]

class CarSessionSerializer(serializers.ModelSerializer):
    class Meta:
        model = CarSession
        fields = ["id", "cars"]