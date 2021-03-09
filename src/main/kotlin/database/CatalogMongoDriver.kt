package database

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import model.Product
import model.User
import rx.Observable

class CatalogMongoDriver(
    url: String
) : CatalogDriver {

    private val client = MongoClients.create(url)

    private val database = client.getDatabase(DATABASE_TITLE)
    private val userCollection = database.getCollection(USER_COLLECTION)
    private val productCollection = database.getCollection(PRODUCT_COLLECTION)

    override fun addUser(user: User): Observable<Success> {
        return userCollection.insertOne(user.asDocument())
    }

    override fun getUser(id: Int): Observable<User> {
        return userCollection.find(eq(User.ID_FIELD, id)).toObservable().map { doc -> User.fromDocument(doc) }
    }

    override fun addProduct(product: Product): Observable<Success> {
        return productCollection.insertOne(product.asDocument())
    }

    override fun getProducts(): Observable<Product> {
        return productCollection.find().toObservable().map { doc -> Product.fromDocument(doc) }
    }

    private companion object {
        const val DATABASE_TITLE = "product_catalog"
        const val PRODUCT_COLLECTION = "product_collection"
        const val USER_COLLECTION = "user_collection"
    }
}