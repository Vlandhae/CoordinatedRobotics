from rest_framework import serializers
from rts.models import Somedata

class SomedataSerializer(serializers.ModelSerializer):
    class Meta:
        model = Somedata
        fields = ["key", "value"]