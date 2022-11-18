from django.urls import path
from rts import views

urlpatterns = [
    path('rts/', views.SomedataList.as_view()),
    path('rts/<key>/', views.SomedataDetail.as_view()),
    path("cars/", views.CarList.as_view()),
    path("cars/sessions/", views.CarSessionList.as_view()),
    path("cars/sessions/<id>/", views.CarSessionDetails.as_view()),
    path("cars/sessions/<id>/cars/", views.EditCarSession.as_view()),  
    path("cars/sessions/<id>/cars/<name>/", views.CarSessionCarsDetail.as_view()),
    path("cars/<name>/", views.CarDetail.as_view()),
    #path("cars/<name>/command/", views.car_current_command),

]