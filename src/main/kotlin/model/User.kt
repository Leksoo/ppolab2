package model

import org.bson.Document

data class User(
    val id: Int,
    val name: String,
    val currency: Currency
) {

    fun asDocument(): Document {
        return Document()
            .append(ID_FIELD, id)
            .append(NAME_FIELD, name)
            .append(CURRENCY_FIELD, currency.name)
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currency='" + currency.name + '\'' +
                '}'
    }


    companion object {
        fun fromDocument(doc: Document) = User(
            doc.getInteger(ID_FIELD),
            doc.getString(NAME_FIELD),
            Currency.valueOf(doc.getString(CURRENCY_FIELD))
        )

        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val CURRENCY_FIELD = "currency"
    }
}