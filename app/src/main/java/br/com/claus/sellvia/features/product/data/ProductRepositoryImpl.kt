package br.com.claus.sellvia.features.product.data

import android.content.Context
import android.net.Uri
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.network.mapSuccess
import br.com.claus.sellvia.core.network.safeApiCall
import br.com.claus.sellvia.core.network.safeDirectCall
import br.com.claus.sellvia.features.product.data.ProductService
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.product.data.model.ProductSearchQuery
import br.com.claus.sellvia.features.product.data.mapper.toDomain
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductRepositoryImpl(
    private val service: ProductService,
    private val context: Context
) : IProductRepository {

    override suspend fun findAll(query: ProductSearchQuery): ResultWrapper<Pagination<Product>> =
        safeDirectCall {
            service.findAll(
                page = query.page,
                perPage = query.perPage,
                sort = query.sort,
                direction = query.direction,
                companyId = query.companyId,
                name = query.name,
                categoryId = query.categoryId,
                minPrice = query.minPrice,
                maxPrice = query.maxPrice,
                minCreatedAt = query.minCreatedAt,
                maxCreatedAt = query.maxCreatedAt,
                minUpdatedAt = query.minUpdatedAt,
                maxUpdatedAt = query.maxUpdatedAt,
                active = query.active,
                createdBy = query.createdBy,
                updatedBy = query.updatedBy,
                id = query.id,
                sku = query.sku
            )
        }.mapSuccess { it.toDomain() }

    override suspend fun create(request: ProductRequest, imageUri: Uri): ResultWrapper<Unit> {
        val imageBytes = context.contentResolver
            .openInputStream(imageUri)
            ?.readBytes()
            ?: return ResultWrapper.Error(message = "Não foi possível ler a imagem selecionada")

        val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
        val imageBody = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", "product_image.jpg", imageBody)
        val dataBody = Gson().toJson(request).toRequestBody("application/json".toMediaType())

        return safeApiCall { service.create(data = dataBody, image = imagePart) }
    }

    override suspend fun update(request: ProductRequest, id: Long): ResultWrapper<Product> {
        return safeApiCall { service.update(request, id) }.mapSuccess { it.toDomain() }
    }

    override suspend fun delete(id: Long): ResultWrapper<Unit> {
        return safeApiCall { service.delete(id) }
    }
}