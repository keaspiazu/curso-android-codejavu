package co.quindio.sena.pruebasentrenamiento;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JsonObjetoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JsonObjetoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JsonObjetoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View vista;
    RequestQueue request;
    Activity actividad;
    JsonObjectRequest jsonObjectRequest;

    RecyclerView recyclerPersonas;
    ArrayList<PersonaVo> listaPersonas;
    PersonasAdapter miAdapter;


    public JsonObjetoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JsonObjetoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JsonObjetoFragment newInstance(String param1, String param2) {
        JsonObjetoFragment fragment = new JsonObjetoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        vista= inflater.inflate(R.layout.fragment_json_objeto, container, false);
        request= Volley.newRequestQueue(this.actividad);
        recyclerPersonas = (RecyclerView)vista.findViewById(R.id.list_json);

        cargarWebService();

        recyclerPersonas.setLayoutManager(new LinearLayoutManager(this.actividad));
        recyclerPersonas.setHasFixedSize(true);

        return vista;
    }

    private void cargarWebService() {
        System.out.println("***********************************************");
        System.out.println("***********************************************");
        System.out.println("INGRESA A CARGAR WS");
        String url = getString(R.string.ip)+"/entrenamiento/wsJSONConsultarListaObjeto.php";
        //String url="http://10.71.134.110/senasoftws/wsJSONConsultarLista.php";
        listaPersonas = new ArrayList<>();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
a                //Toast.makeText(actividad, "Conectados: "+response, Toast.LENGTH_LONG).show();
                System.out.println("***********************************************");
                System.out.println("***********************************************");
                System.out.println("Conectado: " + response);

                try {
                    JSONArray jsonArray = response.optJSONArray("persona");
                    PersonaVo miPersona;
                    for(int i=0;i<jsonArray.length();i++){
                        miPersona=new PersonaVo();
                        JSONObject jresponse =  jsonArray.getJSONObject(i);
                        miPersona.setId(Integer.parseInt(jresponse.getString("id")));
                        miPersona.setNombre(jresponse.getString("nombre"));
                        miPersona.setEdad(Integer.parseInt(jresponse.getString("edad")));
                        miPersona.setTelefono(jresponse.getString("telefono"));
                        miPersona.setProfesion(jresponse.getString("profesion"));
                        miPersona.setDireccion(jresponse.getString("direccion"));
                        listaPersonas.add(miPersona);
                    }

                    miAdapter=new PersonasAdapter(listaPersonas);
                    recyclerPersonas.setAdapter(miAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERRORRRR: " + volleyError);
                Toast.makeText(actividad, "ERRORRRR: " + volleyError, Toast.LENGTH_LONG).show();
                System.out.println("***********************************************");
                System.out.println("***********************************************");
            }
        });

        request.add(jsonObjectRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.actividad=(Activity) context;
            //interfaceComunicaFragment=(IComunicaFragments) this.actividad;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
