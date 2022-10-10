# Stress test 2

# Members

- Sebastian Garcia
- Alejandra Diaz


## Results

| N  | M  |  ms |
|---|---|---|
| 1e3 | 4  | 328  |
| 1e4  | 4  | 498  |
| 1e5 | 4  | 543  |
| 1e6 | 4  |987  |
| 1e7 | 4  | 1392  |
| 1e8 | 4  | 3183  |

## Conclusions

The response is quite fast given the large amount of fibonacci requested, it fails to respond in time when the number of clients grows because the server has to respond one by one instead of being asynchronously.