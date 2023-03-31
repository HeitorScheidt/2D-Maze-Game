package packets;

import main.GameClient;
import net.GameServer;

public abstract class Packet {
     
    public static enum PackTypes{
        INVALID(-1), LOGIN(00), DISCONNECT(01);

        private int packetId;

        private PackTypes(int packetId){
            this.packetId = packetId;
        }

        public int getId(){
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId){
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(GameClient client); //envia para o servidor
    public abstract void writeData(GameServer server); //envia para os clientes

    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();

    public static PackTypes lookupPacket(String packetId){
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PackTypes.INVALID;
        }
    }
    public static PackTypes lookupPacket(int id){
        for(PackTypes p : PackTypes.values()){
            if(p.getId() == id){
                return p;
            }
        }
        return PackTypes.INVALID;
    }
}
