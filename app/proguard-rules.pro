# ─── BuildConfig ──────────────────────────────────────────────────────────────
-keep class **.BuildConfig { *; }

# ─── Retrofit ─────────────────────────────────────────────────────────────────
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# ─── OkHttp ───────────────────────────────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ─── Gson ─────────────────────────────────────────────────────────────────────
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ─── Modelos desserializados pelo Gson ────────────────────────────────────────
# DTOs de resposta e request
-keep class br.com.claus.sellvia.**.data.model.** { *; }
# Modelos de domínio
-keep class br.com.claus.sellvia.**.domain.model.** { *; }
# CRÍTICO: Pagination está em ui.components mas é desserializada pelo Gson
-keep class br.com.claus.sellvia.ui.components.paginationTemplate.models.** { *; }
# Core models (enums UserRole, Direction, CardStyle usados em respostas da API)
-keep class br.com.claus.sellvia.core.model.** { *; }
# Login response (inclui UserLoginResponse e CompanyLoginResponse aninhados)
-keep class br.com.claus.sellvia.features.login.data.model.** { *; }

# ─── Enums ────────────────────────────────────────────────────────────────────
# Gson desserializa enums pelo name() — preservar nomes dos valores é obrigatório
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    <fields>;
}

# ─── Koin ─────────────────────────────────────────────────────────────────────
-keep class org.koin.** { *; }
-keepnames class * { @org.koin.core.annotation.* *; }

# ─── Coroutines ───────────────────────────────────────────────────────────────
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ─── DataStore ────────────────────────────────────────────────────────────────
-keep class androidx.datastore.** { *; }

# ─── Stack traces legíveis ────────────────────────────────────────────────────
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile
