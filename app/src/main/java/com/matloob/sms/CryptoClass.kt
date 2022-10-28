package com.matloob.sms

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoClass {
    // Key specs
    private var secretKeySpec: SecretKeySpec? = null
    // Cipher
    private var cipher: Cipher? = null

    // ivSpec for CBC
    private var ivSpec: IvParameterSpec? = null

    init {
        Security.addProvider(BouncyCastleProvider())
        try {
            secretKeySpec = SecretKeySpec(encryptionKey, "AES")
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ivSpec = IvParameterSpec(encryptionKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    fun encryptMessage(data: String): String {
        // Initialize the cipher for encryption
        cipher?.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        // Encrypt the text
        val textEncrypted = cipher!!.doFinal(data.toByteArray())
        // Convert bytes to string
        return String(textEncrypted, StandardCharsets.ISO_8859_1)
    }

    @Throws(Exception::class)
    fun decryptMessage(data: String): String {
        // Initialize the same cipher for decryption
        cipher?.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        // get encrypted text
        val encryptedBytes = data.toByteArray(StandardCharsets.ISO_8859_1)
        // Decrypt the text
        val textDecrypted = cipher!!.doFinal(encryptedBytes)
        // Convert bytes to string
        return String(textDecrypted)
    }

    companion object {
        // Secret key
        private val encryptionKey =
            byteArrayOf(20, 34, -32, 6, 20, 34, -32, 63, 20, 5, -7, 63, 9, 34, -32, 63)
    }
}