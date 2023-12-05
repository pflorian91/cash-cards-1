# Cash cards

## How to run the application

> mvn clean package

> java -jar target/cash-cards-0.0.1-SNAPSHOT.jar

Application available at `localhost:8061/cashcards`

Get all cash cards
> curl --location 'localhost:8061/cashcards' \
--header 'Authorization: Basic c2FyYWgxOmFiYzEyMw=='

Get cash card by ID
> curl --location 'localhost:8061/cashcards/99' \
--header 'Authorization: Basic c2FyYWgxOmFiYzEyMw=='

Create cash card
> curl --location 'localhost:8061/cashcards' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic c2FyYWgxOmFiYzEyMw==' \
--data '{
"amount": 22.21
}'

Update cash card
> curl --location --request PUT 'localhost:8061/cashcards/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic c2FyYWgxOmFiYzEyMw==' \
--data '{
"amount": 33.31
}'

Delete cash card 
> curl --location --request DELETE 'localhost:8061/cashcards/1' \
--header 'Authorization: Basic c2FyYWgxOmFiYzEyMw=='