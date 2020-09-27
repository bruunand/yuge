# Yuge
Sends a yuge amount of HTTP requests to a single endpoint in a short window of time by withholding the last byte of each request.
Use this research tool to test potential race condition exploits in your own software.
Can be considered a standalone/Dockerized version of PortSwiggers's Turbo Intruder extension. Check out their [article](https://portswigger.net/research/turbo-intruder-embracing-the-billion-request-attack) and [extension](https://github.com/PortSwigger/turbo-intruder) on GitHub, which provides additional functionality for advanced users.

## Usage
Build the Docker image:
`docker build . -t yuge:latest`

Run:
`docker run --read-only -v ${PWD}/samples:/root/samples -e TARGET_HOST=localhost -e TARGET_PORT=5000 -e NUMBER_OF_REQUESTS=100 -e REQUEST_FILE=sample.request yuge:latest`

The example above sends `100` requests to `localhost:5000` using the contents of `samples/sample.request`.