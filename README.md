**Memory Error:**

`
ERROR: [1] bootstrap checks failed
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
`

**Solution:**

https://github.com/elastic/elasticsearch-docker/issues/92

sudo sysctl -w vm.max_map_count=262144



**Elastic Search**

https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

**Elastic4s**

https://github.com/sksamuel/elastic4s

**Elastic4s-JSON-Circe**

https://search.maven.org/#search%7Cga%7C1%7Celastic4s-json-circe