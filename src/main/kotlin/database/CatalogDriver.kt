package database

import com.mongodb.rx.client.Success
import model.Product
import model.User
import rx.Observable

interface CatalogDriver {
    fun addUser(user: User): Observable<Success>
    fun getUser(id: Int): Observable<User>
    fun addProduct(product: Product): Observable<Success>
    fun getProduct(id: Int): Observable<Product>
    fun getProducts(): Observable<Product>
}