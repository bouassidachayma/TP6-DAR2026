package corbaServer;

import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import service.BanqueImpl;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import javax.naming.Context;
import javax.naming.InitialContext;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            // a. Initialiser l'ORB
            ORB orb = ORB.init(args, null);

            // b. Initialiser l'adaptateur d'objets (POA) et l'activer
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // d. Créer le servant (l'implémentation)
            BanqueImpl banqueImpl = new BanqueImpl();

            // e. Enregistrer sa référence dans l'ORB
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(banqueImpl);
            IBanqueRemote href = IBanqueRemoteHelper.narrow(ref);

            // c. Configurer l'annuaire JNDI et enregistrer l'objet
            Context ctx = new InitialContext();
            ctx.rebind("BanqueService", href);

            System.out.println("Serveur CORBA Banque en ligne et en attente...");

            // f. Mettre l'ORB en attente de requêtes des clients
            orb.run();

        } catch (Exception e) {
            System.err.println("Erreur Serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}