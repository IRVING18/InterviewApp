package demo.layout.com.testapplicatiion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.layout.com.testapplicatiion.aidldemo.ClientActivity
import demo.layout.com.testapplicatiion.aidldemo2.MainClientAActivity
import demo.layout.com.testapplicatiion.base.BaseFragment
import demo.layout.com.testapplicatiion.bigimg.BigImgTestActivity
import demo.layout.com.testapplicatiion.boardcastTest.BroadCastTestActivity
import demo.layout.com.testapplicatiion.contraint.ConstraintTestActivity
import demo.layout.com.testapplicatiion.datastore.multiProcess.MultiActivity
import demo.layout.com.testapplicatiion.datastore.preference.PreferencesActivity
import demo.layout.com.testapplicatiion.datastore.proto.ProtoActivity
import demo.layout.com.testapplicatiion.jobscheduler.JobActivity
import demo.layout.com.testapplicatiion.motionlayout.CoordinatorActivity
import demo.layout.com.testapplicatiion.motionlayout.TouchScrollActivity
import demo.layout.com.testapplicatiion.motionlayoutcompose.MotionMenuActivity
import demo.layout.com.testapplicatiion.servicetest.ServiceTestActivity
import demo.layout.com.testapplicatiion.view.text.CustomText
import demo.layout.com.testapplicatiion.view.text.SpanBean
import demo.layout.com.testapplicatiion.workmanager.WorkTestActivity
import demo.layout.com.testapplicatiion.youthTest.YouthTestActivity

/**
 * Created by wangzheng on 8/17/21 8:26 PM.
 * E-mail : ivring11@163.com
 */
class Fragment1 : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater?.inflate(R.layout.fragment_1, null)
        return inflate
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = view?:return
        view.findViewById<View>(R.id.bt1).setOnClickListener {
            val intent = Intent(activity, RoundIndicatorActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt2).setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt3).setOnClickListener {
            val intent = Intent(activity, ClientActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt4)
            .setOnClickListener { //                Intent intent = new Intent(getActivity(), SubsamplingActivity.class);
                val intent = Intent(activity, BigImgTestActivity::class.java)
                startActivity(intent)
            }
        view.findViewById<View>(R.id.bt5).setOnClickListener {
            val intent = Intent(activity, ServiceTestActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt6).setOnClickListener {
            val intent = Intent(activity, BroadCastTestActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt7).setOnClickListener {
            val intent = Intent(activity, WorkTestActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt8).setOnClickListener {
            val intent = Intent(activity, JobActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt9).setOnClickListener {
            val intent = Intent(activity, PreferencesActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt10).setOnClickListener {
            val intent = Intent(activity, ProtoActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt11).setOnClickListener {
            val intent = Intent(activity, MultiActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt12).setOnClickListener {
            val intent = Intent(activity, TouchScrollActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt13).setOnClickListener {
            val intent = Intent(activity, CoordinatorActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt14).setOnClickListener {
            val intent = Intent(activity, MainClientAActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt15).setOnClickListener {
            val intent = Intent(activity, YouthTestActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt133).setOnClickListener {
            val intent = Intent(activity, MotionMenuActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.bt16).setOnClickListener {
            val intent = Intent(activity, ConstraintTestActivity::class.java)
            startActivity(intent)
        }

        val customText = view.findViewById<CustomText>(R.id.cusT)
        val spanList = mutableListOf<SpanBean>()
        spanList.add(SpanBean("el", "#000000", 24f))
        spanList.add(SpanBean("e", "#E02020", 24f))
        spanList.add(SpanBean("ctro", "#000000", 24f))
        spanList.add(SpanBean("e", "#E02020", 24f))
        spanList.add(SpanBean("ncephalograp", "#000000", 24f))
        val strSpiltList = mutableListOf<String>()
        strSpiltList.add("elec")
        strSpiltList.add("troence")
        strSpiltList.add("phalo")
        strSpiltList.add("graphy")
        customText.bindData(spanList, strSpiltList)
    }

}
