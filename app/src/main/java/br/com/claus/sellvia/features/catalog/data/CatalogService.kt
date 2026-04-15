package br.com.claus.sellvia.features.catalog.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogService {

    @retrofit2.http.Streaming
    @GET("companies/{companyId}/catalog/pdf")
    suspend fun downloadCatalog(
        @Path("companyId") companyId: Long,
        @Query("name") name: String? = null,
        @Query("categoryId") categoryId: Long? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("active") active: Boolean? = null,
        @Query("sku") sku: String? = null,
        @Query("type") type: String? = null,
        @Query("sort") sort: String? = null,
        @Query("direction") direction: String? = null,
        @Query("showProductImages") showProductImages: Boolean? = null,
        @Query("showCompanyLogo") showCompanyLogo: Boolean? = null,
        @Query("showCompanyBusinessName") showCompanyBusinessName: Boolean? = null,
        @Query("showCompanyCnpj") showCompanyCnpj: Boolean? = null,
        @Query("showCompanyWebsite") showCompanyWebsite: Boolean? = null,
        @Query("showStats") showStats: Boolean? = null,
        @Query("showAveragePrice") showAveragePrice: Boolean? = null,
        @Query("showActiveCount") showActiveCount: Boolean? = null,
        @Query("showTypeBreakdown") showTypeBreakdown: Boolean? = null,
        @Query("showSku") showSku: Boolean? = null,
        @Query("showStock") showStock: Boolean? = null,
        @Query("showProductionCost") showProductionCost: Boolean? = null,
        @Query("showCategory") showCategory: Boolean? = null,
        @Query("showDescription") showDescription: Boolean? = null,
        @Query("showStatus") showStatus: Boolean? = null,
    ): Response<ResponseBody>
}
