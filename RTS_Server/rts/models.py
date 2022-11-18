from django.db import models

class Somedata(models.Model):
    key = models.TextField()
    value = models.TextField()

class Car(models.Model):
    name = models.TextField(primary_key = True)
    current_command = models.CharField(max_length=1)
    # this has to be cleared whenever the car session disconnects
    channel_name = models.TextField(default="")


class CarSession(models.Model):
    id = models.TextField(primary_key = True, blank=False )
    cars = models.ManyToManyField(Car, blank=True)