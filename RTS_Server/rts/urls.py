from django.urls import path
from rts import views

urlpatterns = [
    path('rts/', views.somedata_list),
    path('rts/<key>/', views.somedata),
]