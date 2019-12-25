package com.wyt.demo.main.fragment.bytefm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.wyt.demo.R
import com.wyt.demo.main.viewmodel.ByteViewModel
import com.wyt.woot_base.fragment.BaseFragment
import com.wyt.woot_base.fragment.EventCenter
import com.wyt.woot_base.util.SpeedUnit
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.byte_fragment.*

class ByteFragment : BaseFragment() {

    companion object {
        fun newInstance() = ByteFragment()
    }

    private lateinit var viewModel: ByteViewModel
    private var durationNum = 1000L
    private var unitValue = SpeedUnit.Mbitps

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.byte_fragment, container, false)
    }

    override fun getBundleExtras(bundle: Bundle) = Unit

    override fun initLogic() = Unit

    override fun initTitle() {
        titleBar.setTitle("流量监听")
            .setLeftImg(R.mipmap.icon_back)
            .setLeftClick(View.OnClickListener {
                onBackThis()
            })
    }

    override fun initView() {
        start.setOnClickListener {
            viewModel.startByteListen(durationNum,unitValue)
        }
        request.setOnClickListener {
            EasyHttp.get("http://video.hrtel.com/video/4G_he.mp4")
                .execute(object :SimpleCallBack<String>(){
                    override fun onSuccess(t: String?) {}

                    override fun onError(e: ApiException?) {}

                })
        }
        stop.setOnClickListener {
            viewModel.stopByteListen()
        }

        select_duration.setOnClickListener {
            val items = arrayOf("1000ms", "2000ms", "3000ms","5000ms")
            QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(0)
                .addItems(items
                ) { dialog, which ->
                    duration.text = items[which]
                    durationNum = items[which].replace("ms","").toLong()
                    dialog.dismiss()
                }.show()
        }

        select_unit.setOnClickListener {
            val items = arrayOf("Mbps", "Kbps", "MB/s","KB/s")
            QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(0)
                .addItems(items
                ) { dialog, which ->
                    unit.text = items[which]
                    unitValue = when(items[which]){
                        "Mbps"->{
                            SpeedUnit.Mbitps
                        }
                        "Kbps"->{
                            SpeedUnit.Kbitps
                        }
                        "MB/s"->{
                            SpeedUnit.MBps
                        }
                        "KB/s"->{
                            SpeedUnit.KBps
                        }
                        else->{
                            SpeedUnit.Mbitps
                        }
                    }
                    dialog.dismiss()
                }.show()
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ByteViewModel::class.java)
        viewModel.byteLiveData.observe(this, Observer {
            value.text = it
        })
    }

    override fun openEventBus(): Boolean = false

    override fun onBack(): Boolean = false

    override fun onEventBus(event: EventCenter<Any>) = Unit

}
