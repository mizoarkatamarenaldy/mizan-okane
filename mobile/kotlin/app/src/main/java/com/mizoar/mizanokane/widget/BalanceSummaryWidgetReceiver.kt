package com.mizoar.mizanokane.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll

class BalanceSummaryWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BalanceSummaryWidget()

    companion object {
        suspend fun updateAll(context: Context) {
            try {
                BalanceSummaryWidget().updateAll(context)
            } catch (_: Throwable) {
                // Abaikan jika widget belum dipasang di layar utama
            }
        }
    }
}
