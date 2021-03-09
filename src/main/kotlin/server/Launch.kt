package server

import database.CatalogMongoDriver

private const val MONGO_HOST = "mongodb://localhost:27017"

fun main() {
    CatalogServer(CatalogMongoDriver(MONGO_HOST)).start()
}