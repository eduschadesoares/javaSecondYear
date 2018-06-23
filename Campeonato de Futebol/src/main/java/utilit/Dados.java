package utilit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Jogo;
import model.Time;
import org.json.JSONObject;

/**
 *
 * @author eduardo
 */
public class Dados {

    private BufferedReader br = null;
    private String nomeArq;
    private Jogo jgLinha;

    List<Time> lstTimes = new ArrayList<>();

    public Dados(String nomeArq) {
        this.nomeArq = nomeArq;
    }

    public List<Jogo> lerJogoTime(Time time) {
        List<Jogo> info = new ArrayList<>();
        Jogo jogoTemp;
        String linha;
        String nomeArq = "JSON\\" + time.getNome() + ".json";
        try {
            br = new BufferedReader(new FileReader(nomeArq));
            while ((linha = br.readLine()) != null) {
                JSONObject eJSON = new JSONObject(linha);

                Integer golA = eJSON.getInt("golA");
                Integer golB = eJSON.getInt("golB");

                String timeA = eJSON.getString("timeA");
                String timeB = eJSON.getString("timeB");

                jogoTemp = new Jogo(timeA, golA.byteValue(), timeB, golB.byteValue());

                info.add(jogoTemp);

            }
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo: " + e.getMessage());
        }
        return info;
    }

    private Time achaTime(String nomeBusca) {
        for (Time t : lstTimes) {
            if (t.getNome().equals(nomeBusca)) {
                return t;
            }
        }
        Time novoTime = new Time(nomeBusca);
        lstTimes.add(novoTime);
        return novoTime;
    }

    public void gravarJogosTime(Time time) {
        try {
            String nomeArq = "JSON\\" + time.getNome() + ".json";

            File file = new File(nomeArq);
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            JSONObject eJSON = new JSONObject();

            for (Jogo j : time.getJogos()) {
                eJSON.put("timeA", j.getTimeA());
                eJSON.put("golA", j.getGolA());
                eJSON.put("timeB", j.getTimeB());
                eJSON.put("golB", j.getGolB());

                bw.write(eJSON.toString() + "\n");
            }

            bw.close();
            System.out.println("Arquivo gravado com sucesso!");

        } catch (Exception e) {
            System.out.println("Não foi possível gravar o arquivo JSON, caminho não existente.");
        }

    }

    public void gravarJogoCamp(String nomeArq) {
        try {

            File file = new File(nomeArq);
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            JSONObject eJSON = new JSONObject();

            for (Time t : lstTimes) {
                for (Jogo j : t.getJogos()) {
                    if (t.getNome().equals(j.getTimeA())) {

                        eJSON.put("timeA", j.getTimeA());
                        eJSON.put("golA", j.getGolA());

                        eJSON.put("timeB", j.getTimeB());
                        eJSON.put("golB", j.getGolB());

                        bw.write(eJSON.toString() + "\n");
                    }
                }
            }

            System.out.println("Salvo com sucesso!");
            bw.close();
        } catch (Exception e) {
            System.out.println("Erro (2): " + e.getMessage());
        }
    }

    private void analisa(Jogo jg) {
        Time posTimeA, posTimeB; // posição times

        posTimeA = achaTime(jg.getTimeA());
        posTimeB = achaTime(jg.getTimeB());

        posTimeA.getJogos().add(jg);
        posTimeB.getJogos().add(jg);

        //System.out.println(posTimeA);
        if (jg.getGolA() > jg.getGolB()) { // A ganhou
            posTimeA.addVit();
            posTimeB.addDer();
        } else if (jg.getGolA() < jg.getGolB()) { // B ganhou
            posTimeB.addVit();
            posTimeA.addDer();
        } else { // empate
            posTimeA.addEmp();
            posTimeB.addEmp();
        }

        posTimeA.addGolP(jg.getGolA());
        posTimeA.addGolN(jg.getGolB());

        posTimeB.addGolP(jg.getGolB());
        posTimeB.addGolN(jg.getGolA());

    }

    public void ordena() {
        Time timeAux;
        int i = 1, j;
        while (i <= lstTimes.size() - 1) {
            j = i;
            while ((j > 0) && ((lstTimes.get(j - 1).getPontos() < lstTimes.get(j).getPontos())
                    || ((lstTimes.get(j - 1).getPontos() == lstTimes.get(j).getPontos())
                    && (lstTimes.get(j - 1).getVit() < lstTimes.get(j).getVit()))
                    || ((lstTimes.get(j - 1).getVit() == lstTimes.get(j).getVit())
                    && (lstTimes.get(j - 1).getSaldoGols() < lstTimes.get(j).getSaldoGols())))) {
                timeAux = lstTimes.get(j - 1);
                lstTimes.set(j - 1, lstTimes.get(j));
                lstTimes.set(j, timeAux);
                j--;
            }
            i++;
        }
    }

    public List<Time> ler() {
        String linha;
        try {
            br = new BufferedReader(new FileReader(nomeArq));
            while ((linha = br.readLine()) != null) {
                if (nomeArq.endsWith(".json")) {

                    JSONObject eJSON = new JSONObject(linha);

                    Integer golA = eJSON.getInt("golA");
                    Integer golB = eJSON.getInt("golB");

                    jgLinha = new Jogo(
                            eJSON.getString("timeA"),
                            golA.byteValue(),
                            eJSON.getString("timeB"),
                            golB.byteValue());

                } else {
                    jgLinha = new Jogo(linha);
                }
                analisa(jgLinha);

            }

            ordena();
            byte i = 1;

            for (Time t : lstTimes) {
                t.setClas(i);
                i++;
            }

        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo (1)");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {

            }
        }

        //System.out.println("nomeArq");
        return lstTimes;
    }

}
