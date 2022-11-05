from django.urls import path
from rts import views

urlpatterns = [
    path('rts/', views.somedata_list),
    path('rts/<key>/', views.somedata),
    path('sessions/', views.session_test),
    path("cars/", views.car_root),
    path("cars/sessions/", views.car_session_root),
    path("cars/sessions/<id>/", views.individual_car_session),
    path("cars/sessions/<id>/cars/", views.edit_car_session),  
    path("cars/<name>/", views.car_individual),
    path("cars/<name>/command/", views.car_current_command),

]