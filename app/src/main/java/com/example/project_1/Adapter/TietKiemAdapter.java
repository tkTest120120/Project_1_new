package com.example.project_1.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_1.DAO.TietKiemDAO;
import com.example.project_1.Models.TietKiem;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.List;

public class TietKiemAdapter extends BaseAdapter {
    Context context;
    List<TietKiem> list = new ArrayList<>();
    TietKiemDAO tietKiemDAO;

    public TietKiemAdapter(Context context, List<TietKiem> list) {
        super();
        this.context = context;
        this.list = list;
        tietKiemDAO = new TietKiemDAO(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler;
        if (convertView == null){
            viewHoler = new ViewHoler();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.show_chung , null);

            viewHoler.maTietKiem = convertView.findViewById(R.id.tv_chung_ma);
            viewHoler.userName = convertView.findViewById(R.id.tv_chung_tk);
            viewHoler.soTienTietKiem = convertView.findViewById(R.id.tv_chung_so_tien);
            viewHoler.ngayTietKiem = convertView.findViewById(R.id.tv_chung_ngay);
            viewHoler.chu_THich = convertView.findViewById(R.id.tv_chung_chu_thich);
//            viewHoler.status = convertView.findViewById(R.id.tv_chung_Status_Tiet_Kiem);
            viewHoler.edit = convertView.findViewById(R.id.img_chung_edit);
            viewHoler.delete = convertView.findViewById(R.id.img_chung_delete);

            ((TextView) convertView.findViewById(R.id.tv_chung_so_1)).setText("T??i Kho???n :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_2)).setText("S??? Ti???n Ti???t Ki???m :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_3)).setText("Ng??y Ti???t Ki???m :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_4)).setText("Ghi ch?? :");
//            ((TextView) convertView.findViewById(R.id.tv_chung_so_5_Tiet_Kiem)).setText("Tr???ng Th??i :");

            viewHoler.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Th??ng b??o").setMessage("B???n c?? ch???c ch???n mu???n x??a m???c Ti???t Ki???m n??y kh??ng ?");
                    builder.setNegativeButton("X??a",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    tietKiemDAO.delete_Tiet_kiem_By_ID( list.get(position).getMaTietKiem() );
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "???? x??a m???c Ti???t Ki???m th??nh c??ng",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.setPositiveButton("H???y", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }

        viewHoler.maTietKiem.setText( list.get(position).getMaTietKiem() );
        viewHoler.userName.setText( list.get(position).getUserName() );
        viewHoler.soTienTietKiem.setText( list.get(position).getSoTienTietKiem() );
        viewHoler.ngayTietKiem.setText( list.get(position).getNgayTietKiem() );
        viewHoler.chu_THich.setText( list.get(position).getChuThich() );

//        if ( list.get(position).getStatus().equalsIgnoreCase( "true" ) ){
//            viewHoler.status.setText("???? Ti???t Ki???m");
//        } else {
//            viewHoler.status.setText("Ch??a Ti???t Ki???m");
//        }

        return convertView;
    }

    private static class ViewHoler{
        TextView maTietKiem;
        TextView userName;
        TextView soTienTietKiem;
        TextView ngayTietKiem;
        TextView chu_THich;
        TextView status;
        ImageView delete;
        ImageView edit;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}
