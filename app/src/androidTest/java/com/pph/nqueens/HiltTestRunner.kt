package com.pph.nqueens

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, ctx: Context?): Application {
        return super.newApplication(cl, "dagger.hilt.android.testing.HiltTestApplication", ctx)
    }
}
