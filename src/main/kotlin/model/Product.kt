package model

import org.bson.Document

data class Product(
    val id: Int,
    val name: String,
    val cost: Double
) {

    fun asDocument(): Document {
        return Document()
            .append(ID_FIELD, id)
            .append(NAME_FIELD, name)
            .append(COST_FIELD, cost)
    }

    override fun toString(): String {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", cost='" + cost + '\'' +
                '}'
    }

    companion object {
        fun fromDocument(doc: Document) = Product(
            doc.getInteger(ID_FIELD),
            doc.getString(NAME_FIELD),
            doc.getDouble(COST_FIELD)
        )

        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val COST_FIELD = "cost"
    }
}