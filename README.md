# HTTP-server
### Simple Java TCP Server that can handle multiple concurrent connections using threading

## Requests handled by the server
- / - responds with **200 OK**
- /echo/_content_ - responds with the content after _/_
- /user-agent - responds with **User-Agent**
- /files/_filename_ - When the server is run with the --directory flag it returns content of a file in that directory for _GET_ requests. The directory is set to "." if no argument is passed.
- /files/_filename_ - If the previous request is of type _POST_ instead of _GET_ it will write content of the request body to a file provided.

## Usage
A client can connect to the server by using curl in the CLI:
```
curl -v localhost:4221/
```
