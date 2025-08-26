# Cards App

This repository contains both the React Frontend **card-ui** and Spring Boot Backend **card-service** for the Cards Application.

## Project Structure
cards-app/  
|--- card-ui/ # React app, UI for creating/searching cards  
|--- card-service/ # Spring Boot REST API for card management  

## API Endpoints
POST /v1/cards/insert → Create a new card  
POST /v1/cards/search → Search cards by full PAN or last 4 digits  

## Features
Create a new card record with Cardholder Name and PAN  
Luhn validation for PAN  
Search cards by full PAN or last 4 digits  

