package com.example.print_html

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.print.PrintHelper

class MainActivity : AppCompatActivity() {

    private var mWebView: WebView? = null
    private val TAG = "WebViewPrint"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPrint = findViewById<Button>(R.id.btn_print)
        btnPrint.setOnClickListener {
            doWebViewPrint()
//            doWebViewPrintURL("https://developer.android.com/about/index.html")
        }
    }

    private fun doWebViewPrint() {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "Trang đã tải xong: $url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

        val htmlDocument = """
            <html>
            <body>
                <h1>Tiêu đề: Tài liệu in thử</h1>
                <p>Đây là nội dung in thử của WebView.</p>
                <img src="dora.png" alt="Example Image" width="300">
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
        webView.loadDataWithBaseURL(
            "file:///android_asset/images/dora.png",
            htmlDocument,                        // Nội dung HTML
            "text/HTML",
            "UTF-8",                         // Mã hóa
            null                             // URL fallback nếu có lỗi
        )

        mWebView = webView
    }
    private fun doWebViewPrintURL(url: String) {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "Trang đã tải xong: $url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

        webView.loadUrl(url)

        mWebView = webView
    }
    private fun createWebPrintJob(webView: WebView) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("Tài liệu của tôi")
        val jobName = "Lệnh in tài liệu"

        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }
}
