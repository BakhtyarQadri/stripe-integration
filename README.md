<h1>Creating Stripe Session</h1>
<br>
curl --location 'http://localhost:8080/checkout/payment/v1/stripe' \
--data '{
    "name": "Product 1",
    "quantity": 1,
    "amount": 100,
    "currency": "USD"
}'

<h1>Creating Stripe Payment Intent</h1>
<br>
curl --location 'http://localhost:8080/checkout/payment/v2/stripe' \
--header 'Content-Type: application/json' \
--data '{
    "amount": 100,
    "currency": "USD"
}'
