package br.com.claus.sellvia.features.registryProduct.data

import android.content.Context
import android.net.Uri
import br.com.claus.sellvia.data.remote.api.ProductService
import br.com.claus.sellvia.data.remote.model.request.ProductRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RegistryProductRepository(
    private val api: ProductService,
    private val context: Context
) {

    suspend fun create(
        request: ProductRequest,
        imageUri: Uri,
        token: String?
    ): Result<Unit> = runCatching {
        val json = Gson().toJson(request)
        val dataBody = json.toRequestBody("application/json".toMediaType())

        val imageBytes = context.contentResolver
            .openInputStream(imageUri)
            ?.readBytes()
            ?: error("Não foi possível ler a imagem selecionada")

        val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
        val imageBody = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", "product_image.jpg", imageBody)

        val response = api.create(
            token = "Bearer $token",
            data = dataBody,
            image = imagePart
        )

        if (!response.isSuccessful) {
            error("Erro ${response.code()}: ${response.errorBody()?.string() ?: "Erro desconhecido"}")
        }
    }
}