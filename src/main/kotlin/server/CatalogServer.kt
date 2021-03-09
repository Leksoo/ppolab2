package server

import database.CatalogDriver
import database.CatalogMongoDriver
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import model.Currency
import model.Product
import model.User
import rx.Observable

object CatalogServer {
    private const val MONGO_HOST = "mongodb://localhost:27017"
    private val catalogDriver: CatalogDriver = CatalogMongoDriver(MONGO_HOST)

    private const val PARAM_ID = "id"
    private const val PARAM_CURRENCY = "currency"
    private const val PARAM_NAME = "name"
    private const val PARAM_COST = "cost"

    private fun handleRequest(name: String, params: Map<String, MutableList<String>>): Observable<String>? {
        return when (name) {
            "sign-up" -> {
                handleSignUp(params)
            }
            "get-products" -> {
                handleGetProducts(params)
            }
            "add-product" -> {
                handleAddProduct(params)
            }
            "get-user" -> {
                handleGetUSer(params)
            }
            else -> {
                null
            }
        }
    }

    private fun handleGetUSer(params: Map<String, MutableList<String>>): Observable<String>? {
        val userId = params[PARAM_ID]?.get(0)?.toInt() ?: return null
        return catalogDriver.getUser(userId).map { it.toString() }
    }

    private fun handleAddProduct(params: Map<String, MutableList<String>>): Observable<String>? {
        val productId = params[PARAM_ID]?.get(0)?.toInt() ?: return null
        val productName = params[PARAM_NAME]?.get(0) ?: return null
        val currency = params[PARAM_CURRENCY]?.get(0)?.let {
            Currency.valueOf(it)
        } ?: return null
        val productCost = params[PARAM_COST]?.get(0)?.toDouble()?.let {
            Currency.convert(it, currency, Currency.USD)
        } ?: return null

        return catalogDriver.addProduct(Product(productId, productName, productCost)).map { it.toString() }
    }

    private fun handleGetProducts(params: Map<String, MutableList<String>>): Observable<String>? {
        val userId = params[PARAM_ID]?.get(0)?.toInt() ?: return null
        val currencyObs = catalogDriver.getUser(userId).map { it.currency }
        val productsObs = catalogDriver.getProducts()
        return currencyObs.flatMap { currency ->
            productsObs.map {
                Product(
                    it.id,
                    it.name,
                    Currency.convert(it.cost, Currency.USD, currency)
                )
            }.map { "$it\n" }
        }
    }

    private fun handleSignUp(params: Map<String, MutableList<String>>): Observable<String>? {
        val userId = params[PARAM_ID]?.get(0)?.toInt() ?: return null
        val userName = params[PARAM_NAME]?.get(0) ?: return null
        val userCurrency = params[PARAM_CURRENCY]?.get(0)?.let {
            Currency.valueOf(it)
        } ?: return null

        return catalogDriver.addUser(User(userId, userName, userCurrency)).map { it.toString() }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        HttpServer
            .newServer(8080)
            .start { req, resp ->
                val name = req.decodedPath.substring(1)
                val response = try {
                    handleRequest(name, req.queryParameters) ?: run {
                        resp.status = HttpResponseStatus.BAD_REQUEST
                        Observable.just("bad request, query parameter is not specified")
                    }
                } catch (e: Exception) {
                    resp.status = HttpResponseStatus.BAD_REQUEST
                    Observable.just("error occurred while processing request\n${e.message}")
                }
                resp.writeString(response)
            }.awaitShutdown()
    }
}




