from django.db import models


class TupleList(models.TextField):
    #__metaclass__ = models.SubfieldBase 
    def __init__(self, *args, **kwargs):
        self.token = kwargs.pop('token', ',')
        self.other_token =  kwargs.pop('other_token', '|')
        super(TupleList, self).__init__(*args, **kwargs)

    def to_python(self, value):
        if not value: return
        if isinstance(value, list):
            return value
        return [tuple(el.split(self.other_token)) for el in value.split(self.token)]

    def get_db_prep_value(self, value, connection, prepared=False):
        print(type(value), value)
        print("value", value)
        value = super().get_db_prep_value(value, connection, prepared)
        if value is None or len(value) == 0:
            return ""
        assert(isinstance(value, list))
        return self.token.join([unicode(self.other_token.join(s)) for s in value])

    def value_to_string(self, obj):
        value = self._get_val_from_obj(obj)
        return self.get_db_prep_value(value)
  

class Somedata(models.Model):
    key = models.TextField()
    value = models.TextField()

class Car(models.Model):
    name = models.TextField(blank=False)
    current_command = models.CharField(max_length=1, default="I")
    # this has to be cleared whenever the car session disconnects
    channel_name = models.TextField(default="")
    connected = models.BooleanField(default=False)


class CarSession(models.Model):
    id = models.TextField(primary_key = True, blank=False )
    cars = models.ManyToManyField(Car, blank=True)

class RTSMap(models.Model):
    session = models.OneToOneField(CarSession, on_delete=models.CASCADE)
    explored_fields = TupleList(blank=True)
    obstacles = TupleList(blank=True)
