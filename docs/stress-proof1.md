# Stress test 1

# Members

- Sebastian Garcia
- Alejandra Diaz

## Results

| N  | M  |  ms |
|---|---|---|
| 1e3 | 4  | 328  |
| 1e4  | 4  | 498  |
| 1e5 | 4  | 2478  |
| 1e6 | 4  | 14328  |
| 1e7 | 4  | 268080  |
| 1e8 | 4  | Timeout  |

## Conclusions

Even though the response is quite fast given the large amount of fibonacci requested, 
it fails to respond in time when the number of clients grows because the server has to respond one by one instead of being asynchronously.